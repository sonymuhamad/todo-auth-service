package com.sony.authservice.interceptor;

import com.sony.authservice.exception.NotFoundException;
import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcGlobalServerInterceptor
public class GlobalGrpcExceptionInterceptor implements ServerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(GlobalGrpcExceptionInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (NotFoundException e) {
                    logger.warn("Handled UserNotFoundException: {}", e.getMessage());
                    call.close(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException().getStatus(), new Metadata());
                } catch (IllegalArgumentException e) {
                    call.close(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException().getStatus(), new Metadata());
                } catch (Exception e) {
                    logger.error("Unhandled gRPC error", e);
                    call.close(Status.INTERNAL.withDescription("Internal error").augmentDescription(e.getMessage()).asRuntimeException().getStatus(), new Metadata());
                }
            }
        };
    }
}
