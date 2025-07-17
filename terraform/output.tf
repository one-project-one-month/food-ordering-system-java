output "rds_endpoint" {
  value = aws_db_instance.app_db.endpoint
}


output "ec2_public_ip" {
  value = aws_instance.public_instance.public_ip
}