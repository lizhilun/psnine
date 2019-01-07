package demo.lizl.com.psnine;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;

@GlideModule
public class GlobalGlideConfiguration extends AppGlideModule
{
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 2000L;//最多可以缓存多少字节的数据
    private static final String DISK_CACHE_NAME = "psnine_glide";

    @Override
    public void applyOptions(Context context, GlideBuilder builder)
    {

        //1.设置Glide内存缓存大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取系统分配给应用的总内存大小
        int memoryCacheSize = maxMemory / 8;//设置图片内存缓存占用八分之一
        //设置内存缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));

        // 2.设置Glide磁盘缓存大小
        File cacheDir = createSavePath();//指定的是数据的缓存地址

        //3.设置磁盘缓存大小
        if (cacheDir != null)
        {
            builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), DISK_CACHE_NAME, DISK_CACHE_SIZE));
        }

        //3.设置BitmapPool缓存内存大小
        builder.setBitmapPool(new LruBitmapPool(memoryCacheSize));
    }

    @Override
    public boolean isManifestParsingEnabled()
    {
        return false;
    }

    /**
     * 创建存储缓存的文件夹
     */
    private File createSavePath()
    {
        String path = Environment.getExternalStorageDirectory().getPath() + "/psnine/imageCache/";

        File file = new File(path);

        if (!file.exists())
        {
            file.mkdirs();
        }
        return file;
    }
}
