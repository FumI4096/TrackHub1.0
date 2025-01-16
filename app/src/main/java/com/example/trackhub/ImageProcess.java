package com.example.trackhub;

import android.content.Context;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.clarifai.channel.ClarifaiChannel;
import io.grpc.Channel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.*;
import com.clarifai.grpc.api.status.StatusCode;

import java.io.ByteArrayOutputStream;
import com.google.protobuf.ByteString;

public class ImageProcess {

    private V2Grpc.V2BlockingStub stub;

    static final String USER_ID = "tkxmwlv2zo9d";
    static final String PAT = "1d3e4a12bf924a9da3441a80504aadd9";
    static final String APP_ID = "image-moderation-aeb01bddedde";
    static final String WORKFLOW_ID = "moderation";

    public ImageProcess(){
        stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
                .withCallCredentials(new ClarifaiCallCredentials(PAT));
    }


    public boolean processImage(Context context, int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);

        // Step 2: Convert the Bitmap to a Base64 String
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        ByteString byteStringImage = ByteString.copyFrom(imageBytes);

        PostWorkflowResultsResponse postWorkflowResultsResponse = stub.postWorkflowResults(
                PostWorkflowResultsRequest.newBuilder()
                        .setUserAppId(UserAppIDSet.newBuilder().setUserId(USER_ID).setAppId(APP_ID))
                        .setWorkflowId(WORKFLOW_ID)
                        .addInputs(
                                Input.newBuilder().setData(
                                        Data.newBuilder().setImage(
                                                Image.newBuilder().setBase64(byteStringImage)
                                        )
                                )
                        )
                        .build()
        );

        // We'll get one WorkflowResult for each input we used above. Because of one input, we have here
        // one WorkflowResult.
        WorkflowResult results = postWorkflowResultsResponse.getResults(0);

        // Each model we have in the workflow will produce one output.
        for (Output output : results.getOutputsList()) {
            Model model = output.getModel();

            Log.d("ImageProcess", "Predicted concepts for the model `" + model.getId() + "`:");
            for (Concept concept : output.getData().getConceptsList()) {
                Log.d("ImageProcess", String.format("\t%s %.2f", concept.getName(), concept.getValue()));

                // Add logic to return false for unsafe categories
                if (concept.getName().equalsIgnoreCase("Drug") && concept.getValue() > 0.9) {
                    return false;
                }
                if (concept.getName().equalsIgnoreCase("Explicit") && concept.getValue() > 0.9) {
                    return false;
                }
                if (concept.getName().equalsIgnoreCase("Gore") && concept.getValue() > 0.9) {
                    return false;
                }
                if (concept.getName().equalsIgnoreCase("Suggestive") && concept.getValue() > 0.9) {
                    return false;
                }
            }
        }

        return true;
    }
}
