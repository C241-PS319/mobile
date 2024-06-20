package com.c241ps319.patera.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.c241ps319.patera.R
import com.c241ps319.patera.data.ResultState
import com.c241ps319.patera.data.model.UserModel
import com.c241ps319.patera.databinding.ActivityLoginBinding
import com.c241ps319.patera.ui.ViewModelFactory
import com.c241ps319.patera.ui.auth.AuthViewModel
import com.c241ps319.patera.ui.auth.register.RegisterActivity
import com.c241ps319.patera.ui.main.MainActivity
import com.c241ps319.patera.ui.main.MainActivity.Companion
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth

    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = viewModel.auth

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLoginWithGoogle.setOnClickListener {
            googleSignIn()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            var isEmptyFields = false
            if (email.isEmpty()) {
                isEmptyFields = true
                binding.edLoginEmail.error = "Tidak Boleh Kosong!"
            }
            if (password.isEmpty()) {
                isEmptyFields = true
                binding.edLoginPassword.error = "Tidak Boleh Kosong!"
            }

            if (!isEmptyFields) {
                viewModel.login(email, password).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Error -> {
                            showToast(result.error.toString())
                            showLoading(false)
                        }

                        is ResultState.Success -> {
                            val token = result.data.data.token
                            viewModel.getUser(token).observe(this) { userResult ->
                                when (userResult) {
                                    is ResultState.Loading -> {
                                        showLoading(true)
                                    }

                                    is ResultState.Error -> {
                                        showToast(userResult.error.toString())
                                        showLoading(false)
                                    }

                                    is ResultState.Success -> {
                                        showLoading(false)
                                        viewModel.saveSession(
                                            UserModel(
                                                name = userResult.data.userData.name,
                                                email = userResult.data.userData.email,
                                                phone = userResult.data.userData.phone,
                                                picture = userResult.data.userData.picture,
                                                token = token,
                                                isLogin = true
                                            )
                                        )
                                        val intent = Intent(this, MainActivity::class.java).apply {
                                            flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        }
                                        showToast("Login sukses!")
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun googleSignIn() {

        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 1)!!
                    .show()
            } else {
                Log.i("Sign-In", "This device is not supported.")
                finish()
            }
        }

        val credentialManager = CredentialManager.create(this) //import from androidx.CredentialManager
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.web_client_id))
            .build()
        val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
            .addCredentialOption(googleIdOption)
            .build()
        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    //import from androidx.CredentialManager
                    request = request,
                    context = this@LoginActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) { //import from androidx.CredentialManager
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result.token
                Log.d(TAG, "updateUI AWALLL: $idToken")
                if (idToken != null) {
                    viewModel.loginGoogle(idToken).observe(this) { result ->
                        when (result) {
                            is ResultState.Loading -> {
                                showLoading(true)
                            }

                            is ResultState.Error -> {
                                showToast(result.error.toString())
                                showLoading(false)
                            }

                            is ResultState.Success -> {
                                val token = result.data.data.token

                                Log.d(TAG, "updateUI: $token")

                                val userModel = UserModel(
                                    name = currentUser.displayName ?: "",
                                    email = currentUser.email ?: "",
                                    phone = currentUser.phoneNumber ?: "",
                                    picture = currentUser.photoUrl.toString(),
                                    token = token,
                                    isLogin = true,
                                    isGoogleLogin = true
                                )

                                viewModel.saveSession(userModel)

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            } else {
                Log.e(TAG, "Error getting ID token", task.exception)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getSession().observe(this) { session ->
            if (session != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.d(TAG, "onStart: Session is null")
            }
        }
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
}