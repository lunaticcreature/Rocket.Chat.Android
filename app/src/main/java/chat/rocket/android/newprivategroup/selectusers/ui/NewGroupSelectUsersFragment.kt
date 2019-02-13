package chat.rocket.android.newprivategroup.selectusers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import chat.rocket.android.R
import chat.rocket.android.helper.Constants
import chat.rocket.android.main.ui.MainActivity
import kotlinx.android.synthetic.main.app_bar.*

class NewGroupSelectUsersFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_new_group_select_users, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupToolbar()
	}

	private fun setupToolbar() {
		(activity as MainActivity).toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
		(activity as MainActivity).toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
		with((activity as AppCompatActivity?)?.supportActionBar) {
			this?.setDisplayShowTitleEnabled(true)
			this?.title = getString(R.string.widechat_new_channel)
		}

		if (Constants.WIDECHAT) {
			with((activity as AppCompatActivity?)?.supportActionBar) {
				this?.setDisplayShowCustomEnabled(false)
			}
		}
	}
}
