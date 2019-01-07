package demo.lizl.com.psnine.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class BaseAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>
{
    private final String TAG = "CommonAdapter";
    private final Object mLock = new Object();
    private Context mContext;
    private View mFooter;
    private RecyclerView mRecyclerView;
    private List<T> mDatas = new ArrayList();

    public BaseAdapter()
    {
    }

    public int getItemCount()
    {
        return this.mDatas != null && this.mDatas.size() != 0 ? (this.mFooter == null ? this.mDatas.size() : this.mDatas.size() + 1) : 0;
    }

    public int getItemViewType(int position)
    {
        return this.getFooter() != null && this.getItemCount() - 1 == position ? 233333 : super.getItemViewType(position);
    }

    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (this.mContext == null)
        {
            this.mContext = parent.getContext();
        }

        if (this.mRecyclerView == null)
        {
            this.mRecyclerView = (RecyclerView) parent;
        }

        return this.createCustomViewHolder(parent, viewType);
    }

    public void onBindViewHolder(BaseViewHolder holder, int position)
    {
        if (this.getItemViewType(position) != 233333)
        {
            this.bindCustomViewHolder((VH) holder, this.mDatas.get(position), position);
        }

    }

    public View inflateView(@LayoutRes int resId, ViewGroup parent)
    {
        return LayoutInflater.from(this.mContext).inflate(resId, parent, false);
    }

    public Context getContext()
    {
        return this.mContext;
    }

    public View getFooter()
    {
        return this.mFooter;
    }

    public void setFooter(View footer)
    {
        this.mFooter = footer;
    }

    public void removeFooter()
    {
        this.mFooter = null;
    }

    public void add(@NonNull T object)
    {
        Object var2 = this.mLock;
        synchronized (this.mLock)
        {
            if (null != this.mDatas)
            {
                this.mDatas.add(object);
                this.notifyItemInserted(this.mDatas.size() - 1);
            }

        }
    }

    public void addAll(@NonNull Collection<? extends T> collection)
    {
        Object var2 = this.mLock;
        synchronized (this.mLock)
        {
            if (null != this.mDatas)
            {
                this.mDatas.addAll(collection);
                if (this.mDatas.size() - collection.size() != 0)
                {
                    this.notifyItemRangeInserted(this.mDatas.size() - collection.size(), collection.size());
                }
                else
                {
                    this.notifyDataSetChanged();
                }
            }

        }
    }

    @SafeVarargs
    public final void addAll(@NonNull T... items)
    {
        Object var2 = this.mLock;
        synchronized (this.mLock)
        {
            if (null != this.mDatas)
            {
                Collections.addAll(this.mDatas, items);
                if (this.mDatas.size() - items.length != 0)
                {
                    this.notifyItemRangeInserted(this.mDatas.size() - items.length, items.length);
                }
                else
                {
                    this.notifyDataSetChanged();
                }
            }

        }
    }

    public void insert(@NonNull T object, int index)
    {
        if (this.mDatas != null && index >= 0 && index <= this.mDatas.size())
        {
            Object var3 = this.mLock;
            synchronized (this.mLock)
            {
                if (null != this.mDatas)
                {
                    this.mDatas.add(index, object);
                    this.notifyItemInserted(index);
                    if (index == 0)
                    {
                        this.scrollTop();
                    }
                }

            }
        }
        else
        {
            Log.i("CommonAdapter", "insert: index error");
        }
    }

    public void insertAll(@NonNull Collection<? extends T> collection, int index)
    {
        if (this.mDatas != null && index >= 0 && index <= this.mDatas.size())
        {
            Object var3 = this.mLock;
            synchronized (this.mLock)
            {
                if (null != this.mDatas)
                {
                    this.mDatas.addAll(index, collection);
                    this.notifyItemRangeInserted(index, collection.size());
                    if (index == 0)
                    {
                        this.scrollTop();
                    }
                }

            }
        }
        else
        {
            Log.i("CommonAdapter", "insertAll: index error");
        }
    }

    public void remove(int index)
    {
        if (this.mDatas != null && index >= 0 && index <= this.mDatas.size() - 1)
        {
            Object var2 = this.mLock;
            synchronized (this.mLock)
            {
                this.mDatas.remove(index);
                this.notifyItemRemoved(index);
            }
        }
        else
        {
            Log.i("CommonAdapter", "remove: index error");
        }
    }

    public boolean remove(@NonNull T object)
    {
        boolean removeSuccess = false;
        if (this.mDatas != null && this.mDatas.size() != 0)
        {
            Object var4 = this.mLock;
            int removeIndex;
            synchronized (this.mLock)
            {
                removeIndex = this.mDatas.indexOf(object);
                removeSuccess = this.mDatas.remove(object);
            }

            if (removeSuccess)
            {
                this.notifyItemRemoved(removeIndex);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            Log.i("CommonAdapter", "remove fail datas emply");
            return false;
        }
    }

    public void clear()
    {
        Object var1 = this.mLock;
        synchronized (this.mLock)
        {
            if (this.mDatas != null)
            {
                this.mDatas.clear();
            }
        }

        this.notifyDataSetChanged();
    }

    public void sort(Comparator<? super T> comparator)
    {
        Object var2 = this.mLock;
        synchronized (this.mLock)
        {
            if (this.mDatas != null)
            {
                Collections.sort(this.mDatas, comparator);
            }
        }

        this.notifyDataSetChanged();
    }

    public void update(@NonNull List<T> mDatas)
    {
        Object var2 = this.mLock;
        synchronized (this.mLock)
        {
            this.mDatas = mDatas;
        }

        this.notifyDataSetChanged();
    }

    public T getItem(int position)
    {
        return this.mDatas.get(position);
    }

    public int getPosition(T item)
    {
        return this.mDatas.indexOf(item);
    }

    public List<T> getData()
    {
        if (null == this.mDatas)
        {
            this.mDatas = new ArrayList();
        }

        return this.mDatas;
    }

    private void scrollTop()
    {
        if (null != this.mRecyclerView)
        {
            this.mRecyclerView.scrollToPosition(0);
        }

    }

    public abstract VH createCustomViewHolder(ViewGroup var1, int var2);

    public abstract void bindCustomViewHolder(VH var1, T var2, int var3);
}