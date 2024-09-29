package org.example.paymentservice.clients;

import org.example.paymentservice.dtos.Order;
import org.example.paymentservice.dtos.User;

public interface IUserServiceClient {
    User getUserDetails(Long customerId);
}
