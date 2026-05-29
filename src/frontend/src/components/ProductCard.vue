<script setup>
import { getProductImageUrl, formatPrice } from '../utils/product'

defineProps({
  product: {
    type: Object,
    required: true,
  },
})
</script>

<template>
  <router-link :to="`/products/${product.id}`" class="product-card">
    <img
      :src="getProductImageUrl(product.imageUrl)"
      :alt="product.name"
      class="product-image"
    />
    <p class="product-name">{{ product.name }}</p>
    <p class="product-price">{{ formatPrice(product.price) }}</p>
  </router-link>
</template>

<style scoped>
.product-card {
  display: block;
  text-decoration: none;
  color: inherit;
  border: 2px solid #111;
  border-radius: 0.75rem;
  padding: 0.75rem;
  background: #fff;
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 4px 4px 0 #111;
}

.product-image {
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
  border: 2px solid #111;
  border-radius: 0.5rem;
  background: #f5f5f5;
}

.product-name,
.product-price {
  margin: 0.75rem 0 0;
  text-align: center;
  font-weight: 600;
  color: #111;
}

.product-price {
  margin-top: 0.35rem;
  font-weight: 500;
}
</style>
