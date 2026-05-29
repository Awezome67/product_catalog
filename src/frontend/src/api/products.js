import http from './http'

export function fetchProducts(params = {}) {
  return http.get('/products', { params })
}

export function fetchProduct(id) {
  return http.get(`/products/${id}`)
}

export function createProduct(product, imageFile) {
  const formData = new FormData()
  formData.append(
    'product',
    new Blob([JSON.stringify(product)], { type: 'application/json' }),
  )
  if (imageFile) {
    formData.append('image', imageFile)
  }
  return http.post('/products', formData)
}

export function updateProduct(id, product, imageFile) {
  const formData = new FormData()
  formData.append(
    'product',
    new Blob([JSON.stringify(product)], { type: 'application/json' }),
  )
  if (imageFile) {
    formData.append('image', imageFile)
  }
  return http.put(`/products/${id}`, formData)
}

export function deleteProduct(id) {
  return http.delete(`/products/${id}`)
}
