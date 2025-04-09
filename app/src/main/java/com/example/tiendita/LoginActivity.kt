package com.example.tiendita

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ComponentActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(email, password)

                // Llamar a RetrofitClient pasando el contexto para obtener la instancia correcta de ApiService
                val call = RetrofitClient.getInstance(this).login(loginRequest)
                call.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            val loginResponse = response.body()

                            // Verificar si la respuesta contiene el token
                            val token = loginResponse?.token
                            if (token != null) {
                                // Guardar el token en SharedPreferences
                                val sharedPreferences = getSharedPreferences("session", MODE_PRIVATE)
                                sharedPreferences.edit().putString("token", token).apply()

                                // Mostrar mensaje de bienvenida
                                Toast.makeText(this@LoginActivity, "Bienvenido ${loginResponse.user.nombre}", Toast.LENGTH_LONG).show()

                                // Navegar al Dashboard
                                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                startActivity(intent)
                                finish() // Cerrar LoginActivity para que no regrese
                            } else {
                                Toast.makeText(this@LoginActivity, "Token no recibido", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("LOGIN_ERROR", "Error de red", t)
                    }
                })
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
