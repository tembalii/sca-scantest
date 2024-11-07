import paramiko
from rest_framework.decorators import api_view

@api_view(["GET", "POST"])
def snippet_list(request):
    tainted = request.GET["query"]
    rand = os.urandom()

    client = paramiko.SSHClient()
    client.load_system_host_keys()
    client.set_missing_host_key_policy(paramiko.WarningPolicy())
    client.connect('localhost', port, username, password)

    # ruleid: tainted-os-command-paramiko-django
    stdin, stdout, stderr = client.exec_command(f"git clone {tainted}")

    # ok: tainted-os-command-paramiko-django
    stdin, stdout, stderr = client.exec_command(f"git clone {rand}")

    # ok: tainted-os-command-paramiko-django
    stdin, stdout, stderr = client.exec_command(f"git clone my-repo")


    client.close()
