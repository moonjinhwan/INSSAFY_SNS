<template>
  <div id="wrap" :class="{ active: getSpinnerActive }">
    <p id="message" class="l-desc">{{ getSpinnerMessage }}</p>
    <div id="spinner" class="center" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'Spinner',
  data() {
    return {};
  },
  computed: {
    ...mapGetters(['getSpinnerActive', 'getSpinnerMessage']),
  },
};
</script>

<style scoped>
#wrap {
  display: none;
  position: fixed;
  width: 100%;
  height: 100%;
  background-color: #695c4cdd;
  color: #fff;
  transition: opacity 0.3s, transform 0.3s ease;
  opacity: 0;
  transform: scale(1.4);
  z-index: -30;
}

.active {
  display: block !important;
  opacity: 1 !important;
  transform: scale(1) !important;
  z-index: 30 !important;
}

p {
  width: 200px;
  height: 14px;
  font-size: 14px;
}

.center {
  position: absolute;
  top: 50%;
  left: 50%;
}
#message {
  position: absolute;
  top: calc(50% - 9px);
  left: calc(50% - 100px);
  text-align: center;
}
#spinner {
  display: inline-block;
  width: 100px;
  height: 100px;
  margin-left: -50px;
  margin-top: -50px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: #fff;
  animation: spin 1s ease-in-out infinite;
  -webkit-animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to {
    -webkit-transform: rotate(360deg);
  }
}
@-webkit-keyframes spin {
  to {
    -webkit-transform: rotate(360deg);
  }
}
</style>
