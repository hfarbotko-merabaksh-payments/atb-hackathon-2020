package ca.atb.hackathon_2020.domain;

import ca.atb.hackathon_2020.Hackathon2020Application;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class Transaction {

    private String id;

    @JsonProperty("other_account")
    private Account targetAccount;

    @JsonProperty("this_account")
    private Account ownAccount;

    private Details details;

    private Metadata metadata;

    @Data
    public static class Details {
        private String type;
        private String description;

        @JsonProperty("posted")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Hackathon2020Application.ISO8601_TIMESTAMP_FORMAT, timezone = "UTC")
        private LocalDateTime postedDate;

        @JsonProperty("completed")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Hackathon2020Application.ISO8601_TIMESTAMP_FORMAT, timezone = "UTC")
        private LocalDateTime completedDate;

        @JsonProperty("new_balance")
        @JsonDeserialize(using = MoneyJson.MoneyDeserializer.class)
        private Money newBalance;

        @JsonProperty("value")
        @JsonDeserialize(using = MoneyJson.MoneyDeserializer.class)
        private Money value;
    }

    @Data
    public static class Metadata {
        private String narrative;
        private List<Object> comments;
        private List<Tag> tags;
        private List<Image> images;

        @JsonProperty("where")
        private Location location;
    }

    @Data
    @NoArgsConstructor
    public static class Tag {
        public Tag(String value) {
            this.value = value;
        }
        private String value;

        private String id;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Hackathon2020Application.ISO8601_TIMESTAMP_FORMAT, timezone = "UTC")
        @JsonProperty("date")
        private Date createdAt;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tag tag = (Tag) o;
            return id.equals(tag.id);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + id.hashCode();
            return result;
        }
    }

    @Data
    public static class Image {
        @JsonProperty("image_URL")
        private String imageUrl;
    }
}
