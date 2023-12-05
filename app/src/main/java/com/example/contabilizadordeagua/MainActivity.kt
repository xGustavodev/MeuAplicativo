package com.example.contabilizadordeagua

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var bt_lembrete: Button
    private lateinit var bt_alarme: Button
    private lateinit var txt_hora: TextView
    private lateinit var txt_minutos: TextView

    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var calendario: Calendar
    var horaAtual = 0
    var minutosAtuais = 0

    private val prefs = ContPreferences(this)

    var today: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_lembrete = findViewById(R.id.btn_lembrete)
        bt_lembrete.setOnClickListener {
            calendario = Calendar.getInstance()
            horaAtual = calendario.get(Calendar.HOUR_OF_DAY)
            minutosAtuais = calendario.get(Calendar.MINUTE)
            timePickerDialog = TimePickerDialog(this, {timePicker: TimePicker, hourOfDay: Int, minutes: Int ->
                txt_hora.text = String.format("%02d",hourOfDay)
                txt_minutos.text = String.format("%02d",minutes)
            },horaAtual,minutosAtuais,true)
            timePickerDialog.show()
        }


        bt_alarme = findViewById(R.id.btn_alarme)
        bt_alarme.setOnClickListener {

            if (!txt_hora.text.toString().isEmpty() && !txt_minutos.text.toString().isEmpty()){
                val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_HOUR, txt_hora.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MINUTES, txt_minutos.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, getString(R.string.mensagem_alarme))
                startActivity(intent)

                if (intent.resolveActivity(packageManager) !=null){
                    startActivity(intent)
                }
            }

        }


        IniciarComp()

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



    private fun IniciarComp() {
        bt_lembrete = findViewById(R.id.btn_lembrete)
        bt_alarme = findViewById(R.id.btn_alarme)
        txt_hora = findViewById(R.id.txt_hr)
        txt_minutos = findViewById(R.id.txt_min)
    }

}