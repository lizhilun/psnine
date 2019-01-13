package demo.lizl.com.psnine.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import demo.lizl.com.psnine.presenter.BasePresenter
import demo.lizl.com.psnine.util.Constant

abstract class BaseActivity<T : BasePresenter<*>> : AppCompatActivity()
{
    protected val TAG = this.javaClass.simpleName

    protected lateinit var presenter: T

    abstract fun getLayoutResId(): Int

    abstract fun initPresenter()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        initPresenter()
        presenter.init()
    }

    fun turnToGameDetailActivity(gameDetailUrl: String)
    {
        val intent = Intent(this, GameDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString(Constant.BUNDLE_DATA_STRING, gameDetailUrl)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun turnToPostDetailActivity(postDetailUrl: String)
    {
        val intent = Intent(this, PostDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString(Constant.BUNDLE_DATA_STRING, postDetailUrl)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun turnToUserDetailActivity(psnId: String)
    {
        val intent = Intent(this, UserDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString(Constant.BUNDLE_DATA_STRING, psnId)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun turnToLoginActivity()
    {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onResume()
    {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onStart()
    {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onRestart()
    {
        Log.d(TAG, "onRestart")
        super.onRestart()
    }

    override fun onPause()
    {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop()
    {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy()
    {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }
}