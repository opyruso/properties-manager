const fs = require('fs');
const path = require('path');

const env = process.env.CONFIG_ENV || 'dev';
const allowed = ['dev', 'rec', 'pro'];
if (!allowed.includes(env)) {
  console.error(`Invalid CONFIG_ENV "${env}" (expected dev, rec, pro).`);
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
