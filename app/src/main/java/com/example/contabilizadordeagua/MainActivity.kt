package com.example.contabilizadordeagua

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var bt_lembrete: Button
    private lateinit var bt_alarme: Button
    private lateinit var txt_hora: TextView
    private lateinit var txt_minutos: TextView
    private lateinit var ic_redefinir_dados: ImageView

    private val prefs = ContPreferences(this)

    var today: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RedefinirDados()

        ic_redefinir_dados.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(R.string.dialog_titulo)
                .setMessage(R.string.dialog_descricao)
                .setPositiveButton("Ok", {dialogInterface, i->
                })
            alertDialog.show()
        }

        findViewById<Button>(R.id.btn_peq).setOnClickListener {
            saveCont(ContType.peq)

        }
        findViewById<Button>(R.id.btn_med).setOnClickListener {
            saveCont(ContType.med)

        }
        findViewById<Button>(R.id.btn_grand).setOnClickListener {
            saveCont(ContType.grand)
        }
        refresh()
    }

    private fun saveCont(contType: ContType) {
        prefs.save(today + contType.value)

        Snackbar.make(findViewById(android.R.id.content), R.string.undo, Snackbar.LENGTH_LONG)
            .setAction(android.R.string.ok) {
                prefs.save(today - contType.value)
                refresh()
            }
            .show()
        refresh()
    }

    private fun refresh() {
        val value = prefs.fetch()
        today = value
        findViewById<TextView>(R.id.txt_total).text = today.toString()
    }


    private fun RedefinirDados() {
        ic_redefinir_dados = findViewById(R.id.ic_redefinir)
    }


}