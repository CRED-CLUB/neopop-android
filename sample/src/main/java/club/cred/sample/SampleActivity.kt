package club.cred.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import club.cred.sample.view.NavigatorFragment

class SampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        supportFragmentManager.commit {
            add(R.id.fr, NavigatorFragment(), "NavigatorFragment")
        }
    }
}
