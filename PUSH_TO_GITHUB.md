# Push to Ashikshett6 GitHub

Your GitHub: https://github.com/Ashikshett6

## Step 1 — Create empty repo (one time)

Open this link and click **Create repository** (do **not** add README, .gitignore, or license):

https://github.com/Ashikshett6/Login_monitoring

## Step 2 — Push from your PC

In PowerShell:

```powershell
cd "C:\Users\dines\Desktop\Login Monitoring"
git push -u origin main
```

## After push

- Repo URL: https://github.com/Ashikshett6/Login_monitoring
- CI runs automatically on the **Actions** tab

## Optional — GitHub CLI (future)

```powershell
gh auth login
gh repo create login-monitoring-system --public --source=. --remote=origin --push
```
