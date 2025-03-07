package org.bank.pay.core.event.product;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.bank.core.payment.Product;

public class ProductToVirtualCashConverter implements Converter<Product, VirtualCash> {
    @Override
    public VirtualCash convert(Product product) {
        return VirtualCash.of(product);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(Product.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(VirtualCash.class);
    }
}
