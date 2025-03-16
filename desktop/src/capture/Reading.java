package capture;

/**
 * A connection speed reading.
 */
public final class Reading {
    /**
     * The location the reading was taken at.
     */
    public Point point;
    /**
     * Download speed in MiB/s.
     */
    public float download = 0.0f;
    /**
     * Upload speed in MiB/s.
     */
    public float upload = 0.0f;
    /**
     * Ping in ms.
     */
    public float ping = 0.0f;

    public Reading(Point point) {
        this.point = point;
    }

    public Reading withDownload(float download) {
        this.download = download;

        return this;
    }

    public Reading withUpload(float upload) {
        this.upload = upload;

        return this;
    }
    public Reading withPing(float ping) {
        this.ping = ping;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        try {
            final var other = (Reading) o;

            return download == other.download
                && upload == other.upload
                && ping == other.ping;
        } catch (ClassCastException __) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        throw new RuntimeException("unimplemented");
    }
}
