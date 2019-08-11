package com.example.foodyappkotlin.screen.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.example.foodyappkotlin.AppSharedPreference
import com.example.foodyappkotlin.R
import com.example.foodyappkotlin.common.BaseActivity
import com.example.foodyappkotlin.data.models.User
import com.example.foodyappkotlin.data.repository.FoodyRepository
import com.example.foodyappkotlin.data.response.UserResponse
import com.example.foodyappkotlin.screen.detail.DetailViewModel
import com.example.foodyappkotlin.screen.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.layout_login_activity.*
import javax.inject.Inject

class LoginActivity : BaseActivity(), View.OnClickListener, LoginInterface.View {

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    lateinit var presenter: LoginInterface.Presenter

    @Inject
    lateinit var repository: FoodyRepository

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login_activity)
        AndroidInjection.inject(this)

        presenter = LoginPresenter(repository, this)
        button_google_login.setOnClickListener(this)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                progressBar.visibility = View.GONE
                showAlertMessage("Yêu cầu thất bại","Vòng lòng kiểm tra lại các kết nối")
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val userGoogle = auth.currentUser
                    if (userGoogle != null) {
                        val user = User(
                            userGoogle.email!!,
                            userGoogle.displayName!!,
                            userGoogle.photoUrl.toString(),
                            2
                        )
                        presenter.saveDataLogin(user)
                    }
                } else {
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        progressBar.visibility = View.VISIBLE
    }

    private fun signOut() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this) {
        }
    }

    private fun revokeAccess() {
        auth.signOut()

        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.button_google_login -> signIn()
        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    override fun userInfoSuccess(user: UserResponse) {
        progressBar.visibility = View.GONE
        appSharedPreference.setToken(user.userId)
        appSharedPreference.setUser(user)
        startActivity(MainActivity.newInstance(this))
        finish()
    }

    override fun serverFailure(msg: String) {
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        signOut()
    }
}