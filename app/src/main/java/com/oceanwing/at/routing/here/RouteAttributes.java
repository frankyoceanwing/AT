package com.oceanwing.at.routing.here;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class RouteAttributes {
    private List<RoutingRequest.RouteAttribute> mAttributes;

    public RouteAttributes(@NonNull RoutingRequest.RouteAttribute attribute) {
        mAttributes = new ArrayList<>();
        mAttributes.add(attribute);
    }

    public RouteAttributes(@NonNull List<RoutingRequest.RouteAttribute> attributes) {
        mAttributes = attributes;
    }

    public List<RoutingRequest.RouteAttribute> getAttributes() {
        return mAttributes;
    }

    @Override
    public String toString() {
        if (mAttributes.size() == 0) {
            return "";
        }
        return String.format("&routeAttributes=%s", values());
    }

    public String values() {
        return TextUtils.join(",", mAttributes.toArray());
    }
}
