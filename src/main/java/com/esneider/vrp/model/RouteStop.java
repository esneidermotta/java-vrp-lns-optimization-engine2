package com.esneider.vrp.model;

public record RouteStop(DeliveryJob job, int arrivalMinute, int departureMinute) {
}
