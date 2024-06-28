<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'App',
  data() {
    return {
    }
  },
  mounted() {
    // Add event listeners for beforeunload and unload
    window.addEventListener('beforeunload', e => this.beforeunloadHandler(e));
    window.addEventListener('unload', e => this.unloadHandler(e));
  },
  unmounted() {
    // Remove event listeners when component is destroyed
    window.removeEventListener('beforeunload', e => this.beforeunloadHandler(e));
    window.removeEventListener('unload', e => this.unloadHandler(e));
  },
  methods: {
    // Handler for beforeunload event
    // eslint-disable-next-line no-unused-vars
    beforeunloadHandler(e) {
      this.clearLogin(); // Call clearLogin beforeunload
      window.close(); // Close window after logout (if needed)
    },
    // Handler for unload event (currently unused)
    unloadHandler() {
      // You can add cleanup code here if needed
    },
    // Axios version of clearLogin method
    clearLogin() {
      const params = {
        sessionId: sessionStorage.getItem('session')
      }
      // Using async/await syntax for asynchronous operations
      axios.post('https://frp-fun.top:52702/user/logout', null, {params})
      .then(response => {
        console.log(response.data);
      })
      .catch(error => {
        console.error('Error logging out:', error);
      });
    }
  }
}
</script>


<style>
/* 添加全局样式 */
</style>
