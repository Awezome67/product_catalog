<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AppHeader from '../components/AppHeader.vue'
import DeleteConfirmModal from '../components/DeleteConfirmModal.vue'
import { deleteProduct, fetchProduct } from '../api/products'
import { getProductImageUrl, formatPrice } from '../utils/product'

const route = useRoute()
const router = useRouter()

const product = ref(null)
const loading = ref(true)
const error = ref('')
const showDeleteModal = ref(false)
const deleting = ref(false)

const productId = computed(() => Number(route.params.id))

async function loadProduct() {
  loading.value = true
  error.value = ''

  try {
    const { data } = await fetchProduct(productId.value)
    product.value = data
  } catch {
    error.value = 'Product not found.'
    product.value = null
  } finally {
    loading.value = false
  }
}

function openDeleteModal() {
  showDeleteModal.value = true
}

function closeDeleteModal() {
  showDeleteModal.value = false
}

async function confirmDelete() {
  if (!product.value || deleting.value) {
    return
  }

  deleting.value = true
  try {
    await deleteProduct(product.value.id)
    router.push('/')
  } catch {
    error.value = 'Failed to delete product. Please try again.'
    closeDeleteModal()
  } finally {
    deleting.value = false
  }
}

onMounted(loadProduct)
</script>

<template>
  <div class="page">
    <AppHeader />

    <div class="page-body">
      <p v-if="loading" class="status">Loading product...</p>
      <p v-else-if="error" class="status error">{{ error }}</p>

      <section v-else-if="product" class="detail-layout">
        <img
          :src="getProductImageUrl(product.imageUrl)"
          :alt="product.name"
          class="detail-image"
        />

        <div class="detail-info">
          <div class="detail-header">
            <h1>{{ product.name }}</h1>
            <div class="icon-actions">
              <router-link
                :to="`/products/${product.id}/edit`"
                class="icon-btn"
                aria-label="Edit product"
                title="Edit"
              >
                ✎
              </router-link>
              <button
                type="button"
                class="icon-btn"
                aria-label="Delete product"
                title="Delete"
                @click="openDeleteModal"
              >
                🗑
              </button>
            </div>
          </div>

          <p class="detail-price">{{ formatPrice(product.price) }}</p>
          <p class="detail-category">{{ product.categoryName }}</p>
          <p class="detail-description">
            {{ product.description || 'No description provided.' }}
          </p>
        </div>
      </section>
    </div>

    <DeleteConfirmModal
      :open="showDeleteModal"
      :product-name="product?.name ?? ''"
      @confirm="confirmDelete"
      @cancel="closeDeleteModal"
    />
  </div>
</template>

<style scoped>
.page {
  min-height: 100vh;
  background: #fff;
}

.page-body {
  padding: 2rem 1.5rem;
}

.detail-layout {
  display: grid;
  grid-template-columns: minmax(280px, 420px) 1fr;
  gap: 2rem;
  align-items: start;
}

.detail-image {
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
  border: 2px solid #111;
  border-radius: 0.75rem;
  background: #f5f5f5;
}

.detail-info {
  border: 2px solid #111;
  border-radius: 0.75rem;
  padding: 1.5rem;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
}

.detail-header h1 {
  margin: 0;
  font-size: 2rem;
  color: #111;
}

.icon-actions {
  display: flex;
  gap: 0.5rem;
}

.icon-btn {
  width: 2.5rem;
  height: 2.5rem;
  border: 2px solid #111;
  border-radius: 0.5rem;
  background: #fff;
  font-size: 1.1rem;
  cursor: pointer;
  display: grid;
  place-items: center;
  text-decoration: none;
  color: inherit;
}

.icon-btn:hover {
  background: #111;
  color: #fff;
}

.detail-price,
.detail-category,
.detail-description {
  margin: 1rem 0 0;
  line-height: 1.5;
}

.detail-price {
  font-size: 1.35rem;
  font-weight: 700;
}

.detail-category {
  color: #444;
}

.status {
  margin: 0;
  padding: 1rem;
  border: 2px dashed #111;
  border-radius: 0.75rem;
  text-align: center;
}

.status.error {
  border-style: solid;
  color: #b71c1c;
}

@media (max-width: 900px) {
  .detail-layout {
    grid-template-columns: 1fr;
  }
}
</style>
