const fs = require('fs');
const path = require('path');

const env = process.env.CONFIG_ENV;
if (!env) {
  console.error('CONFIG_ENV env var not set (expected dev, rec, pro).');
  process.exit(1);
}

const configDir = path.join(__dirname, '..', 'public', 'config');
const mappings = [
  { src: `config_${env}.json`, dest: 'config.json' },
  { src: `keycloak_${env}.json`, dest: 'keycloak.json' }
];

for (const { src, dest } of mappings) {
  const srcPath = path.join(configDir, src);
  const destPath = path.join(configDir, dest);
  if (!fs.existsSync(srcPath)) {
    console.error(`Source file not found: ${srcPath}`);
    process.exit(1);
  }
  fs.copyFileSync(srcPath, destPath);
  console.log(`Copied ${src} to ${dest}`);
}
