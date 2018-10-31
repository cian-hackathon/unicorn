import base64
import uuid

def hello_pubsub(event, context):
    """Triggered from a message on a Cloud Pub/Sub topic.
    Args:
         event (dict): Event payload.
         context (google.cloud.functions.Context): Metadata for the event.
    """
    unicorn_status = base64.b64decode(event['data']).decode('utf-8')
    save_unicorn_status('unicorn-221011', unicorn_status)

def save_unicorn_status(project_id, unicorn_status):
    # [START datastore_quickstart]
    # Imports the Google Cloud client library
    from google.cloud import datastore

    # Instantiates a client
    datastore_client = datastore.Client(project_id)

    # The kind for the new entity
    kind = 'Task'
    # The name/ID for the new entity
    name = str(uuid.uuid4())
    # The Cloud Datastore key for the new entity
    task_key = datastore_client.key(kind, name)

    # Prepares the new entity
    task = datastore.Entity(key=task_key)
    task['status'] = unicorn_status

    # Saves the entity
    datastore_client.put(task)

    print('Saved {}: {}'.format(task.key.name, task['status']))