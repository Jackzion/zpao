<template>
  <div>
    <van-uploader
        v-model="fileList"
        :after-read="onAfterRead"
        :max-count="1"
        accept="image/*"
    />
    <div v-if="imageUrl" class="image-preview">
      <img :src="imageUrl" alt="Uploaded Image" />
      <p>图片的 Blob URL: {{ imageUrl }}</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      fileList: [], // 用于存储上传的文件列表
      imageUrl: null, // 用于存储图片的本地路径
    };
  },
  methods: {
    onAfterRead(file) {
      // 将上传的文件转换为本地 URL
      this.imageUrl = URL.createObjectURL(file.file);
      // 如果您需要文件的内容而不是 URL，可以使用 FileReader
      const reader = new FileReader();
      reader.onload = (e) => {
        console.log('File content:', e.target.result); // 这里可以获得文件的 base64 内容
      };
      reader.readAsDataURL(file.file);
    },
  },
};
</script>

<style scoped>
.image-preview img {
  max-width: 100%;
  height: auto;
  display: block;
  margin-top: 10px;
}
</style>
