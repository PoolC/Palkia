package org.poolc.api.notification.repository;

import org.poolc.api.member.domain.Member;
import org.poolc.api.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.receiverId = :receiverId AND n.readStatus = :readStatus")
    List<Notification> findByReceiverIdAndReadStatus(@Param("receiverId") String receiverId, @Param("readStatus") Boolean readStatus);

    @Query("SELECT n FROM Notification n WHERE n.receiverId = :receiverId ORDER BY n.createdAt DESC")
    List<Notification> findAllByReceiverId(@Param("receiverId") String receiverId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.receiverId = :receiverId AND n.readStatus = false")
    Long countByReceiverIdAndReadIsFalse(@Param("receiverId") String receiverId);
}
