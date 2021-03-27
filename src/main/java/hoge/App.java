package hoge;

import java.util.Locale;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;

public class App {

	/*
	 * 参考URL
	 * https://docs.microsoft.com/en-us/azure/storage/blobs/storage-quickstart-blobs-java?tabs=powershell#configure-your-storage-connection-string
	 * https://github.com/Azure/azure-sdk-for-java/blob/master/sdk/storage/azure-storage-blob/src/samples/java/com/azure/storage/blob/BasicExample.java#L38
	 */
	public static void main(String... args) {
		
		//ストレージ アカウント名
		String accountName = "<YOUR_ACCOUNT_NAME>";
		//アクセスキーのkey1の値
		String accountKey = "<YOUR_ACSSES_KEY_VALUE_1>";
		
		StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);
		
		//ストレージアカウントの「プロパティ」のblobのURLを指定する
		String endpoint = String.format(Locale.ROOT, "<YOUR_STORAGE_ACCOUNT_BLOB_URL>", accountName);
		
		//Blobサービスの大元のクライアントAPIを作成する
		BlobServiceClient storageClient = new BlobServiceClientBuilder().endpoint(endpoint).credential(credential).buildClient();
		
		//Blob内で使用されるコンテナの実態を表すクライアントAPIを作成する。
		BlobContainerClient blobContainerClient = storageClient.getBlobContainerClient("mtgback-stor01-container01-eval-jpe");

		//コンテナクライアントAPIを使用して、ファイルの実体（この場合Blob自体）を取得する。
		BlobClient blobClient = blobContainerClient.getBlobClient("test.txt");
		
		//ローカル上にファイルをダウンロードする。
		blobClient.downloadToFile("./" + "test.txt");
		
		//ダウンロード完了後、Quarkusにアップロードされたファイルを削除する。（設計としては、リネームしてBlobとして再配備するなどいくつか設計パターンはある）
		blobClient.delete();
		
		/*
		 * 特にクライアントAPIでクローズ処理とかないので、何も実装しない。（通信はHTTPS）
		 * Azure Blob StorageのAPIの内部はNullPointerException位しかスローしない。
		 * 上記のため、エラーハンドリングを実装しない。
		 * 
		 * blobClient.downloadToFile(pasu)、実行時にUncheckedIOExceptionがスローされる可能性はあるが、
		 * ダウンロード後は、通常のFile関連のJavaのAPIなどになるので、ダウンロードしたファイルの存在チェックなどを実装し、
		 * 自前でエラーハンドリングを実装することとなる。
		 */
	}
}
