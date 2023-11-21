package cmc.mellyserver.dbcore.scrap;

public enum ScrapType {

    FRIEND("친구"),
    FAMILY("가족"),
    COUPLE("연인"),
    COMPANY("동료");

    private String description;

    ScrapType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
