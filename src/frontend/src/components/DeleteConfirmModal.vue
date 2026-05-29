<script setup>
defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  productName: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['confirm', 'cancel'])
</script>

<template>
  <div v-if="open" class="modal-backdrop" @click.self="emit('cancel')">
    <div class="modal" role="dialog" aria-modal="true" aria-labelledby="delete-title">
      <h2 id="delete-title">Delete product?</h2>
      <p>
        Are you sure you want to delete
        <strong>{{ productName }}</strong>?
      </p>
      <div class="actions">
        <button type="button" class="btn yes" @click="emit('confirm')">Yes</button>
        <button type="button" class="btn no" @click="emit('cancel')">No</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: grid;
  place-items: center;
  z-index: 1000;
  padding: 1rem;
}

.modal {
  width: min(100%, 420px);
  background: #fff;
  border: 2px solid #111;
  border-radius: 0.75rem;
  padding: 1.5rem;
  box-shadow: 8px 8px 0 #111;
}

.modal h2 {
  margin: 0 0 0.75rem;
  color: #111;
}

.modal p {
  margin: 0 0 1.25rem;
  line-height: 1.5;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
}

.btn {
  min-width: 88px;
  padding: 0.55rem 1rem;
  border: 2px solid #111;
  border-radius: 0.5rem;
  font: inherit;
  font-weight: 600;
  cursor: pointer;
}

.btn.yes {
  background: #111;
  color: #fff;
}

.btn.no {
  background: #fff;
  color: #111;
}
</style>
