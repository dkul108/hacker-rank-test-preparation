package org.cv.test.diff;


import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobBinary {

    private final Blob blob;

    public BlobBinary(Blob blob) {
        this.blob = blob;
    }

    //TODO - @Override
    public byte[] getBytes() {
        if ( blob == null) {
            return null ;
        }
        try {
            return blob.getBytes( 1, (int) blob.length());
        } catch (SQLException e) {
            throw new IllegalStateException( e);
        }
    }

    //TODO - @Override
    public InputStream getInputStream () {
        try {
            return blob.getBinaryStream() ;
        } catch (SQLException e) {
            throw new IllegalStateException( e);
        }
    }

    //TODO - @Override
    public long length () {
        try {
            return blob.length() ;
        } catch (SQLException e) {
            throw new IllegalStateException( e);
        }
    }


}
