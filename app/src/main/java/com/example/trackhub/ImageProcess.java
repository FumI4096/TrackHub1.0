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

    public ImageProcess(){
        stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
                .withCallCredentials(new ClarifaiCallCredentials("0d877058bd4f4fc0b7152f4011f020df"));
    }

    public boolean processImage(Context context, int drawableId){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);

        // Step 2: Convert the Bitmap to a Base64 String
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        ByteString byteStringImage = ByteString.copyFrom(imageBytes);

        MultiOutputResponse response = stub.postModelOutputs(
                PostModelOutputsRequest.newBuilder()
                        .setModelId("aaa03c23b3724a16a56b629203edc62c")
                        .addInputs(
                                Input.newBuilder().setData(
                                        Data.newBuilder().setImage(
                                                Image.newBuilder().setBase64(byteStringImage)
                                        )
                                )
                        )
                        .build()
        );

        if (response.getStatus().getCode() != StatusCode.SUCCESS) {
            throw new RuntimeException("Request failed, status: " + response.getStatus());
        }

        for (Concept c : response.getOutputs(0).getData().getConceptsList()) {
            Log.d("ClarifaiResponse", String.format("%12s: %,.2f", c.getName(), c.getValue()));

            if (c.getName().equals("violence") && c.getValue() > 0.9) {
                return false;
            }
            if (c.getName().equals("sexual") && c.getValue() > 0.9) {
                return false;
            }
        }

        return true;
    }
}
