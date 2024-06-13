package org.sanlam.services;

import org.sanlam.events.WithdrawalEvent;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.math.BigDecimal;

public class SnsService {
    private SnsClient snsClient;

    public SnsService() {
        this.snsClient = SnsClient.builder()
            .region(Region.EU_WEST_1) // Specify your region
            .build();
    }

    public void publishWithdrawalEvent(BigDecimal amount, Long accountId, String status) {
        WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "SUCCESSFUL");
        String eventJson = event.toJson(); // Convert event to JSON
        String snsTopicArn = "arn:aws:sns:YOUR_REGION:YOUR_ACCOUNT_ID:YOUR_TOPIC_NAME";

        PublishRequest publishRequest = PublishRequest.builder()
                .message(eventJson)
                .topicArn(snsTopicArn)
                .build();

        PublishResponse publishResponse = snsClient.publish(publishRequest);
    }
}
