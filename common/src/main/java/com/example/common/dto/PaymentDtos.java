package com.example.common.dto;

import com.example.common.types.PaymentStatus;
import java.util.UUID;

public final class PaymentDtos { private PaymentDtos() {}
    public record PaymentDto(Long id, UUID orderId, String reference,
                             Long amountCents, PaymentStatus status) {}
    public record PaymentSubmitRequest(UUID orderId, Long amountCents, String reference) {}
    public record PaymentUpdateRequest(Long paymentId, PaymentStatus status) {}
    public record PaymentReverseRequest(Long paymentId, String reason) {}
}
