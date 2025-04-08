package no.ntnu.gr10.bacheloraccesscontrolbackend;

public class EntityViews {
    public interface IdOnly {
    }   // Only ID fields are visible

    public interface Full extends IdOnly {
    }  // Full view includes all fields

    public interface NoId {
    }

    public interface HidePassword {
    }
}
