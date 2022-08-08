package com.example.hmscorekits

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hmscorekits.databinding.FragmentSignInBinding
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService


class SignInFragment : Fragment() {
    private lateinit var _binging: FragmentSignInBinding
    private val bindig get() = _binging
    private var mAuthManager: AccountAuthService? = null
    private var mAuthParam: AccountAuthParams? = null
    private var singInResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(result.data)
            if (result.resultCode == Activity.RESULT_OK) {

                if (authAccountTask.isSuccessful) {
                    val authAccount = authAccountTask.result
                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                    //  Toast.makeText(requireContext(),authAccount.email.toString(),Toast.LENGTH_LONG).show()
                    //printMessage("SignIn with Huawei Id Sucess \nName: ${authAccount.displayName} \nAccessToken:${authAccount.accessToken}")
                } else {
                    Toast.makeText(
                        requireContext(),
                        authAccountTask.exception.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e(
                        "TAG",
                        "signIn failed: " + (authAccountTask.exception as ApiException).statusCode
                    )
                }
            }
            try {
                Toast.makeText(requireContext(), authAccountTask.result.email, Toast.LENGTH_LONG)
                    .show()
            } catch (exception: Exception) {
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binging = FragmentSignInBinding.inflate(layoutInflater, container, false)
        bindig.btnAccountSignin.setOnClickListener {
            signIn()
        }
        // findNavController().navigate(R.id.action_signInFragment_to_homeFragment)

        return bindig.root
    }

    private fun signIn() {
        mAuthParam = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setAuthorizationCode()
            .createParams()
        mAuthManager = AccountAuthManager.getService(requireActivity(), mAuthParam)
        singInResultLauncher.launch(mAuthManager?.signInIntent)
    }

}