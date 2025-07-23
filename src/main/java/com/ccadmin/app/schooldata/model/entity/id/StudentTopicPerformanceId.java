package com.ccadmin.app.schooldata.model.entity.id;

import java.io.Serializable;
import java.util.Objects;

public class StudentTopicPerformanceId implements Serializable {

    public String StudentID;
    public int TopicID;

    public StudentTopicPerformanceId() {}

    public StudentTopicPerformanceId(String StudentID, int TopicID) {
        this.StudentID = StudentID;
        this.TopicID = TopicID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentTopicPerformanceId)) return false;
        StudentTopicPerformanceId that = (StudentTopicPerformanceId) o;
        return TopicID == that.TopicID &&
                Objects.equals(StudentID, that.StudentID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(StudentID, TopicID);
    }

    @Override
    public String toString() {
        return "StudentTopicPerformanceId{" +
                "StudentID='" + StudentID + '\'' +
                ", TopicID=" + TopicID +
                '}';
    }
}
