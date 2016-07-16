
<?
if($_GET['usun']=='1')
{
	file_put_contents ( "ip.txt", '');
}
else 
{
	$ip=$_SERVER['REMOTE_ADDR'];
	file_put_contents ( "ip.txt", $ip);
}
?>