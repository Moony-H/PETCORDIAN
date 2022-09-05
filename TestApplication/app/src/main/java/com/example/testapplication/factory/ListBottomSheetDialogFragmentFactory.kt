import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.testapplication.view.ListBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ListBottomSheetDialogFragmentFactory(private val title:String, private val list:List<String>,private val onItemClicked:(String, Int,View)->Unit):FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ListBottomSheetFragment::class.java.name -> ListBottomSheetFragment(title, list,onItemClicked)
            else -> super.instantiate(classLoader, className)
        }
    }

}