

<template>
  <el-dropdown trigger="hover">
    <div class="userinfo">
      <template v-if="!userinfo">
        <i class="el-icon-user" />
        admin
      </template>
      <template v-else>
        <img class="avatar" :src="userinfo.avatar" />
        {{ userinfo.name }}
      </template>
    </div>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item>{{ $t('topbar.center') }}</el-dropdown-item>
        <el-dropdown-item>{{ $t('topbar.password') }}</el-dropdown-item>
        <lock-modal />
        <el-dropdown-item @click="logout">
          {{ $t('topbar.logout') }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>
<script>
import { useRouter } from 'vue-router'
import { useUserinfo } from '@/components/Avatar/hooks/useUserinfo'
import LockModal from './LockModal.vue'
import { useApp } from '@/pinia/modules/app'

import { defineComponent, getCurrentInstance } from 'vue'
import { Logout } from '@/api/login'

export default defineComponent({
  components: {
    LockModal,
  },
  setup() {
    const router = useRouter()

    const { userinfo } = useUserinfo()

    const { proxy: ctx } = getCurrentInstance() // 可以把ctx当成vue2中的this

    // 退出
    const logout = async () => {
      const { code, data, message } = await Logout()
      if (code == 200) {
        // 清除token
        useApp().clearToken()
        router.push('/login')
      } else {
        ctx.$message.error(message)
      }
    }

    return {
      userinfo,
      logout,
    }
  },
})
</script>

<style lang="scss" scoped>
.userinfo {
  padding: 0 16px;
  line-height: 48px;
  cursor: pointer;
  display: flex;
  align-items: center;
  &:hover {
    background: #f5f5f5;
  }
  .el-icon-user {
    font-size: 20px;
    margin-right: 8px;
  }
  .avatar {
    margin-right: 8px;
    width: 32px;
    height: 32px;
    border-radius: 50%;
  }
}
</style>
