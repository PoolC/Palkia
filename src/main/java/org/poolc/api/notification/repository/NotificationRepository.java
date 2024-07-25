package org.poolc.api.notification.repository;

import org.poolc.api.member.domain.Member;
import org.poolc.api.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiverIdAndReadStatus(String receiverId, Boolean readStatus);

    List<Notification> findAllByReceiverId(String receiverId);

    Long countByReceiverIdAndReadIsFalse(String receiverId);
}
