package com.example.tiendita.carrito

                    import android.content.Intent
                    import android.net.Uri
                    import android.os.Bundle
                    import android.os.CountDownTimer
                    import android.view.View
                    import android.widget.Button
                    import android.widget.EditText
                    import android.widget.ProgressBar
                    import android.widget.TextView
                    import androidx.appcompat.app.AppCompatActivity
                    import com.example.tiendita.R
                    import com.example.tiendita.data.RetrofitClient
                    import retrofit2.Call
                    import retrofit2.Callback
                    import retrofit2.Response
                    import android.widget.Toast
                    import com.example.tiendita.data.PaymentValidationRequest
                    import com.example.tiendita.data.PaymentValidationResponse

                    class ShippingFormActivity : AppCompatActivity() {

                        private lateinit var etNombre: EditText
                        private lateinit var etDireccion: EditText
                        private lateinit var etCiudad: EditText
                        private lateinit var etCP: EditText
                        private lateinit var etPais: EditText
                        private lateinit var btnPagar: Button
                        private lateinit var progressBarPayment: ProgressBar
                        private lateinit var tvPaymentStatus: TextView
                        private lateinit var carrito: ArrayList<CarritoItem>

                        override fun onCreate(savedInstanceState: Bundle?) {
                            super.onCreate(savedInstanceState)
                            setContentView(R.layout.activity_shipping_form)

                            // Obtener el carrito del intent
                            carrito = intent.getSerializableExtra("carrito") as? ArrayList<CarritoItem> ?: arrayListOf()

                            etNombre = findViewById(R.id.etNombre)
                            etDireccion = findViewById(R.id.etDireccion)
                            etCiudad = findViewById(R.id.etCiudad)
                            etCP = findViewById(R.id.etCP)
                            etPais = findViewById(R.id.etPais)
                            progressBarPayment = findViewById(R.id.progressBarPayment)
                            tvPaymentStatus = findViewById(R.id.tvPaymentStatus)
                            btnPagar = findViewById(R.id.btnPagar)

                            btnPagar.setOnClickListener {
                                if (validarFormulario()) {
                                    iniciarProcesoPago()
                                }
                            }
                        }

                        private fun validarFormulario(): Boolean {
                            // Validación básica del formulario
                            if (etNombre.text.isEmpty() || etDireccion.text.isEmpty() ||
                                etCiudad.text.isEmpty() || etCP.text.isEmpty() || etPais.text.isEmpty()) {
                                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                                return false
                            }
                            return true
                        }

                        private fun iniciarProcesoPago() {
                            val api = RetrofitClient.getInstance(this)

                            // Mostrar estado de procesamiento
                            tvPaymentStatus.visibility = View.VISIBLE
                            tvPaymentStatus.text = "Creando orden de pago..."
                            btnPagar.isEnabled = false

                            // Hacer la llamada para crear la orden de PayPal
                            api.crearOrdenPayPal().enqueue(object : Callback<PayPalResponse> {
                                override fun onResponse(call: Call<PayPalResponse>, response: Response<PayPalResponse>) {
                                    if (response.isSuccessful && response.body() != null) {
                                        val url = response.body()!!.approveUrl
                                        tvPaymentStatus.text = "Redirigiendo a PayPal..."

                                        // Abrir navegador para PayPal
                                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        startActivity(browserIntent)

                                        // Después de que el usuario apruebe el pago, inicia el temporizador
                                        tvPaymentStatus.text = "Procesando pago..."
                                        progressBarPayment.visibility = View.VISIBLE
                                        startPaymentTimer(response.body()!!.id)
                                    } else {
                                        tvPaymentStatus.text = "Error al crear orden"
                                        btnPagar.isEnabled = true
                                        Toast.makeText(this@ShippingFormActivity, "Error al crear orden", Toast.LENGTH_LONG).show()
                                    }
                                }

                                override fun onFailure(call: Call<PayPalResponse>, t: Throwable) {
                                    tvPaymentStatus.text = "Error de red"
                                    btnPagar.isEnabled = true
                                    Toast.makeText(this@ShippingFormActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
                                }
                            })
                        }

                        // Temporizador para simular la verificación del pago
                        private fun startPaymentTimer(orderID: String) {
                            val totalTime = 10000L // 10 segundos
                            val interval = 100L // Actualización cada 100ms para animación suave

                            val timer = object : CountDownTimer(totalTime, interval) {
                                override fun onTick(millisUntilFinished: Long) {
                                    // Calcular progreso como porcentaje
                                    val progress = ((totalTime - millisUntilFinished) * 100 / totalTime).toInt()
                                    progressBarPayment.progress = progress
                                }

                                override fun onFinish() {
                                    progressBarPayment.progress = 100
                                    validatePayment(orderID)
                                }
                            }

                            timer.start()
                        }

                        // Función para validar el pago después del temporizador
                        private fun validatePayment(orderID: String) {
                            tvPaymentStatus.text = "Validando pago..."

                            object : CountDownTimer(2000, 2000) {
                                override fun onTick(millisUntilFinished: Long) {}

                                override fun onFinish() {
                                    // Simulamos que el pago siempre es validado correctamente
                                    tvPaymentStatus.text = "¡Pago validado exitosamente!"
                                    clearCartAndMarkAsPurchased()
                                }
                            }.start()
                            val api = RetrofitClient.getInstance(this)

                            val validationRequest = PaymentValidationRequest(orderID)

                            api.validatePayment(validationRequest).enqueue(object : Callback<PaymentValidationResponse> {
                                override fun onResponse(call: Call<PaymentValidationResponse>, response: Response<PaymentValidationResponse>) {
//                                    if (response.isSuccessful && response.body() != null) {
//                                        // Mostrar confirmación
//                                        tvPaymentStatus.text = "¡Pago validado exitosamente!"
//                                        // Lógica para vaciar el carrito y marcar como comprado
//                                        clearCartAndMarkAsPurchased()
//                                    } else {
//                                        tvPaymentStatus.text = "Error al validar el pago"
//                                        btnPagar.isEnabled = true
//                                        Toast.makeText(this@ShippingFormActivity, "Error al validar el pago", Toast.LENGTH_LONG).show()
//                                    }
                                }

                                override fun onFailure(call: Call<PaymentValidationResponse>, t: Throwable) {
//                                    tvPaymentStatus.text = "Error de red"
//                                    btnPagar.isEnabled = true
//                                    Toast.makeText(this@ShippingFormActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
                                }
                            })
                        }

                        // Función para vaciar el carrito y marcarlo como comprado
                        private fun clearCartAndMarkAsPurchased() {
                            // Mostrar confirmación
                            tvPaymentStatus.text = "¡Compra completada exitosamente!"

                            // Simular espera para mostrar confirmación
                            object : CountDownTimer(2000, 2000) {
                                override fun onTick(millisUntilFinished: Long) {}

                                override fun onFinish() {
                                    // Redirigir a una pantalla de confirmación o al inicio
                                    val intent = Intent()
                                    intent.putExtra("compraRealizada", true)
                                    setResult(RESULT_OK, intent)
                                    finish() // Cerrar esta actividad para evitar volver atrás
                                }
                            }.start()
                        }
                    }