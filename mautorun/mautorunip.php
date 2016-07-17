
<?
if($_GET['usun']=='1')
{
	file_put_contents ( "ip", '');
}
else 
{
	$ip=$_SERVER['REMOTE_ADDR'];
	file_put_contents ( "ip", $ip);
}
?>