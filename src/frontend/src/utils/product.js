const DEFAULT_IMAGE = '/image.png'

export function getProductImageUrl(imageUrl) {
  if (!imageUrl) {
    return DEFAULT_IMAGE
  }
  return imageUrl
}

export function formatPrice(price) {
  const value = Number(price)
  if (Number.isNaN(value)) {
    return '$0.00'
  }
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(value)
}
