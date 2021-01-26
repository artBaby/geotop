package com.geotop.geotopproject.loader.geosplitter.model;

public class GeoArea {
    private GeoPoint upLeftPoint;
    private GeoPoint upRightPoint;
    private GeoPoint bottomLeftPoint;
    private GeoPoint bottomRightPoint;

    private GeoPoint center;

    public GeoArea(GeoPoint upLeftPoint, GeoPoint upRightPoint, GeoPoint bottomLeftPoint, GeoPoint bottomRightPoint) {
        this.upLeftPoint = upLeftPoint;
        this.upRightPoint = upRightPoint;
        this.bottomLeftPoint = bottomLeftPoint;
        this.bottomRightPoint = bottomRightPoint;
    }

    public GeoPoint getUpLeftPoint() {
        return upLeftPoint;
    }

    public void setUpLeftPoint(GeoPoint upLeftPoint) {
        this.upLeftPoint = upLeftPoint;
    }

    public GeoPoint getUpRightPoint() {
        return upRightPoint;
    }

    public void setUpRightPoint(GeoPoint upRightPoint) {
        this.upRightPoint = upRightPoint;
    }

    public GeoPoint getBottomLeftPoint() {
        return bottomLeftPoint;
    }

    public void setBottomLeftPoint(GeoPoint bottomLeftPoint) {
        this.bottomLeftPoint = bottomLeftPoint;
    }

    public GeoPoint getBottomRightPoint() {
        return bottomRightPoint;
    }

    public void setBottomRightPoint(GeoPoint bottomRightPoint) {
        this.bottomRightPoint = bottomRightPoint;
    }

    public GeoPoint getCenter() {
        return this.center;
    }

    public void setCenter(GeoPoint center) {
        this.center = center;
    }

    @Override
    public String toString() {
        return "upLeft: " + upLeftPoint.toString() +"\n" +
                "upRight: " + upRightPoint.toString() +"\n" +
                "bottomLeft: " + bottomLeftPoint.toString() +"\n" +
                "bottomRight: " + bottomRightPoint.toString() +"\n" +
                "center: " + ((center == null) ? null : center.toString()) +"\n";
    }
}
