<template>
  <van-skeleton title avatar :row="3" :loading="props.loading" v-for="user in props.userList">
    <van-card
        :desc="user.profile"
        :title="`${user.username}（${user.planetCode}）`"
        :thumb="user.avatarUrl"
    >
      <template #tags>
        <van-tag plain type="danger" v-for="tag in user.tags" style="margin-right: 8px; margin-top: 8px">
          {{ tag }}
        </van-tag>
      </template>
      <template #footer>
        <van-button v-if="type===0" size="mini" @click="doAdd(user.id)">添加</van-button>
        <van-button v-if="type===1" size="small" @click="doAdmit(user.id)">接受</van-button>
        <van-button v-if="type===1" size="small" type="danger" @click="doDelete(user.id)">拒绝</van-button>
        <van-button v-if="type===2" size="small" type="danger" @click="doDelete(user.id)">删除</van-button>
      </template>
    </van-card>
  </van-skeleton>
</template>

<script setup lang="ts">
import {UserType} from "../models/user";
import myAxios from "../plugins/myAxios";
import { Toast } from "vant";

interface UserCardListProps {
  type:number;
  loading: boolean;
  userList: UserType[];
}
// 传入 props 
const props = withDefaults(defineProps<UserCardListProps>(), {
  // 默认 type 0--> 添加 ， type 1-->接受，拒绝
  type:0 ,
  loading: true,
  // @ts-ignore
  userList: [] as UserType[],
});

// 添加好友
const doAdd = async (userId:number)=> {
  if (!userId) {
    return;
  }
  alert(userId);
  const res = await myAxios.post('/friend/add', {
    toUserId: userId
  });
  if (res?.code === 0) {
    Toast.success('添加成功');
  } else {
    Toast.fail('添加失败' + (res.description ? `，${res.description}` : ''));
  }
}

// 同意好友
const doAdmit = async (userId:number)=> {
  if (!userId) {
    return;
  }
  alert(userId);
  const res = await myAxios.post('/friend/admit', {
    toUserId: userId
  });
  if (res?.code === 0) {
    Toast.success('已同意');
  } else {
    Toast.fail('添加失败' + (res.description ? `，${res.description}` : ''));
  }
}

// 拒绝（删除）好友
const doDelete = async (userId:number)=> {
  if (!userId) {
    return;
  }
  const res = await myAxios.post('/friend/delete', {
    toUserId: userId
  });
  if (res?.code === 0) {
    Toast.success('已删除');
  } else {
    Toast.fail('删除失败' + (res.description ? `，${res.description}` : ''));
  }
}

</script>

<style scoped>

</style>
