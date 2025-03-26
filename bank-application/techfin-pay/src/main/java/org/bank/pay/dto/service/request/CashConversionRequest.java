package org.bank.pay.dto.service.request;

import org.bank.core.cash.Money;

import java.util.UUID;

public record CashConversionRequest(UUID cardId, Money amount) { }
