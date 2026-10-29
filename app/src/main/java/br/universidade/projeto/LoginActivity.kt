package br.universidade.projeto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // Recebe o resultado da tela do FirebaseUI
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result -> onSignInResult(result) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.btnEntrar).setOnClickListener { abrirLogin() }

        // Já está logado? Vai direto para a tela principal
        if (FirebaseAuth.getInstance().currentUser != null) {
            irParaMain()
            return
        }

        if (savedInstanceState == null) abrirLogin()
    }

    private fun abrirLogin() {
        val providers = arrayListOf(
            // E-mail: se o e-mail não existir, o FirebaseUI abre o CADASTRO
            AuthUI.IdpConfig.EmailBuilder()
                .setAllowNewAccounts(true)
                .setRequireName(true)
                .build(),
            // Google (precisa do SHA-1 cadastrado no Firebase)
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_logo)
            .setTheme(R.style.Theme_LoginFirebase)
            .setIsSmartLockEnabled(false)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse

        if (result.resultCode == RESULT_OK) {
            if (response?.isNewUser == true) {
                Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
            }
            irParaMain()
        } else {
            val msg = when {
                response == null -> "Login cancelado"
                response.error?.errorCode == ErrorCodes.NO_NETWORK -> "Sem conexão com a internet"
                else -> "Erro: ${response.error?.message}"
            }
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    private fun irParaMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
