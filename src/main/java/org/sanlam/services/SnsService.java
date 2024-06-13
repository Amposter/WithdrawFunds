package org.sanlam.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.math.BigDecimal;

import org.sanlam.events.WithdrawalEvent;


public class SnsService {
    @Autowired
    private SnsClient snsClient;

    @Value("${aws.snsTopicArn}")
    private String snsTopicArn;

    public void publishWithdrawalEvent(BigDecimal amount, Long accountId, String status) {
        WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "SUCCESSFUL");
        String eventJson = event.toJson(); // Convert event to JSON

        PublishRequest publishRequest = PublishRequest.builder()
                .message(eventJson)
                .topicArn(snsTopicArn)
                .build();

        PublishResponse publishResponse = snsClient.publish(publishRequest);
    }
}
