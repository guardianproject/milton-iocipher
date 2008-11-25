package com.ettrema.http.fs;

import com.bradmcevoy.http.CopyableResource;
import com.bradmcevoy.http.DeletableResource;
import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.MoveableResource;
import com.bradmcevoy.http.PropFindableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.io.StreamToStream;
import eu.medsea.util.MimeUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 */
public class FsFileResource extends FsResource implements CopyableResource, DeletableResource, GetableResource, MoveableResource, PropFindableResource{

    public FsFileResource(FileSystemResourceFactory factory, File file) {
        super(factory, file);
    }

    public Long getContentLength() {
        return file.length();
    }

    public String getContentType(String preferredList) {
        String mime = MimeUtil.getMimeType(file);
        return MimeUtil.getPreferedMimeType(preferredList, mime);        
    }

    public String checkRedirect(Request arg0) {
        return null;
    }

    public void sendContent(OutputStream out, Range range, Map<String, String> params) throws IOException {
        FileInputStream in = new FileInputStream(file);
//        if( range != null ) {
//            long start = range.getStart();
//            if( start > 0 ) in.skip(start);
//            long finish = range.getFinish();
//            if( finish > 0 ) {
//                StreamToStream.readTo(in, out);
//            }
//        } else {
            IOUtils.copy(in, out);
//        }
    }

    public Long getMaxAgeSeconds() {
        return factory.getMaxAgeSeconds();
    }

    @Override
    protected void doCopy(File dest) {
        try {
            FileUtils.copyFile(file, dest);
        } catch (IOException ex) {
            throw new RuntimeException("Failed doing copy to: " + dest.getAbsolutePath(), ex);
        }
    }
    
}