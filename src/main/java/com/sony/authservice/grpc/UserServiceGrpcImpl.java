package com.sony.authservice.grpc;

import com.sony.authservice.model.User;
import com.sony.authservice.service.UserService;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserService userService;

    @Override
    public void getUserByID(GetUserByIDRequest request,
                            io.grpc.stub.StreamObserver<GetUserByIDResponse> responseObserver) {
        // Dummy data — replace with DB call
        if (request.getId().isBlank()) {
            throw new IllegalArgumentException("User ID is required");
        }

        User user = userService.getById(request.getId());

        GetUserByIDResponse response = GetUserByIDResponse.newBuilder()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
