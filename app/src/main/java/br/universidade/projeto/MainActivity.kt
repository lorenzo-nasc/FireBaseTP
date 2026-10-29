package br.universidade.projeto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            voltarParaLogin()
            return
        }

        findViewById<TextView>(R.id.tvNome).text = "Olá, ${user.displayName ?: "usuário"}!"
        findViewById<TextView>(R.id.tvEmail).text = user.email ?: ""
        findViewById<TextView>(R.id.tvUid).text = "UID: ${user.uid}"

        // Botão SAIR -> desloga e volta para a tela de login
        findViewById<Button>(R.id.btnSair).setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener { voltarParaLogin() }
        }
    }

    private fun voltarParaLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
