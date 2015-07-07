package pl.java.scalatech.metrics;

import java.nio.file.FileStore;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;

import lombok.extern.slf4j.Slf4j;

import com.codahale.metrics.health.HealthCheck;

@Slf4j
public class DiskCapacityHealthCheck extends HealthCheck {
    private static final long K = 1024;
    private static final long M = K * K;
    private static final long G = M * K;

    @Override
    protected Result check() throws Exception {
        for (Path root : FileSystems.getDefault().getRootDirectories()) {
            try {
                FileStore store = Files.getFileStore(root);
                if (store.getUsableSpace() / G >= 1) { 
                    return HealthCheck.Result.healthy("disk resources : " + "available="
                        + readableFileSize(store.getUsableSpace()) + ", total=" + readableFileSize(store.getTotalSpace())); 
                    }
                return HealthCheck.Result.unhealthy("not enough  disk space " + "available=" + readableFileSize(store.getUsableSpace()) + ", total="
                        + readableFileSize(store.getTotalSpace()));
            } catch (FileSystemException e) {
                return HealthCheck.Result.unhealthy(e);
            }
        }
        return HealthCheck.Result.healthy();
    }

    public static String readableFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
